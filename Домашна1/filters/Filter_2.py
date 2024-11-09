import os.path

from Домашна1.filters.FilterData import *

class Filter2DataSaver(DataFilter):
    def process_company_chunk(self, companies_subset):
        dates = {}
        for company in companies_subset:
            if data_file_exists(company):
                dates[company] = self.get_last_transaction_date(company)

            else:
                dates[company] = parse_string_from_date(datetime.now())
                self.save_company_data(company)
        return dates


    def apply(self, data):
        print("Старт Филтер 2")
        num_threads = os.cpu_count() * 2
        chunk_size = ceil(len(data) / num_threads)
        data_chunks = [data[i:i + chunk_size] for i in range(0, len(data), chunk_size)]
        path = os.path.join('.', 'database')
        if not os.path.exists(path):
            os.mkdir(path)

        all_dates = {}
        with concurrent.futures.ThreadPoolExecutor(max_workers=num_threads) as executor:
            futures = [executor.submit(self.process_company_chunk, chunk) for chunk in data_chunks]
            for future in concurrent.futures.as_completed(futures):
                all_dates.update(future.result())


        return all_dates



    def get_last_transaction_date(self, company_name):
        path = os.path.join('.', 'database', f'{company_name}.xlsx')
        data_frame = pd.read_excel(path)
        return data_frame.iloc[0, 0]

    def save_company_data(self, company_name):
        output_dir = os.path.join('.', 'database', f'{company_name}.xlsx')
        empty_data = {
            "Датум": [],
            "Цена на последна трансакција": [],
            "Мак.": [],
            "Мин.": [],
            "Просечна цена": [],
            "%пром.": [],
            "Количина": [],
            "Промет во БЕСТ во денари": [],
            "Вкупен промет во денари": [],
        }
        excel_data = pd.DataFrame(empty_data)
        for i in range(10):
            url = self.build_url(company_name, i)
            soup = fetch_soup_from_url(url)
            data_frame = extract_table_data(soup)

            if data_frame is None:
                break

            excel_data = pd.concat([excel_data, data_frame], ignore_index=True)

        excel_data.to_excel(output_dir, index=False)

    def build_url(self, company, i):
        url = 'https://www.mse.mk/mk/stats/symbolhistory/' + company.lower()
        days = 364

        now = datetime.now()
        days_to_subtract = days * i
        to_date = now - timedelta(days=days_to_subtract)
        from_date = to_date - timedelta(days=days)

        url += f'?FromDate={parse_string_from_date(from_date)}&ToDate={parse_string_from_date(to_date)}&Code={company}'

        return url
