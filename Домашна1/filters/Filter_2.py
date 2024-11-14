import concurrent.futures
import os.path

from filters.FilterData import *

cpu_cores = os.cpu_count()
num_threads = cpu_cores * 2

class Filter2DataSaver(DataFilter):
    def apply(self, data):
        print("Start filter 2")
        chunk_size = ceil(len(data) / num_threads)
        data_chunks = [data[i:i + chunk_size] for i in range(0, len(data), chunk_size)]
        storage_path = os.path.join('.', 'database')
        if not os.path.exists(storage_path):
            os.mkdir(storage_path)
        all_dates = {}
        with concurrent.futures.ProcessPoolExecutor(max_workers=cpu_cores) as executor:
            results = list(executor.map(self.process_company_data_batch, data_chunks))
            for result in results:
                all_dates.update(result)
        return all_dates

    def process_data_in_batches(self, data_chunk):
        aggregated_results = {}
        with concurrent.futures.ThreadPoolExecutor(max_workers=num_threads) as thread_executor:
            chunk_size = ceil(len(data_chunk) / num_threads)
            data_chunks = [data_chunk[i:i + chunk_size] for i in range(0, len(data_chunk), chunk_size)]

            results = list(thread_executor.map(self.process_company_data_batch, data_chunks))
            for result in results:
                aggregated_results.update(result)
        return aggregated_results

    def process_company_data_batch(self, company_batch):
        company_dates = {}
        for company in company_batch:
            if data_file_exists(company):
                company_dates[company] = self.retrieve_last_transaction_date(company)
            else:
                company_dates[company] = parse_string_from_date(datetime.now())
                self.save_last_decade_data_to_csv(company)
        return company_dates

    def retrieve_last_transaction_date(self, company_name):
        file_path = os.path.join('.', 'database', f'{company_name}.csv')
        with open(file_path, mode="r", newline='') as file:
            csv_reader = csv.reader(file)
            next(csv_reader)
            first_data_row = next(csv_reader)
            return first_data_row[0]

    def save_last_decade_data_to_csv(self, company_name):
        output_file_path = os.path.join('.', 'database', f'{company_name}.csv')
        csv_data = [
            ["Date", "Price of last transaction", "Max.", "Min.", "Average price", "%prom.", "Quantity", "BEST turnover in denars", "Total turnover in denars"]
        ]
        session = requests.Session()

        for year_offset in range(10):
            url = self.data_url(company_name, year_offset)
            soup = fetch_soup_with_session(url, session)
            table_data = extract_table_data_from_soup(soup)

            if table_data is None:
                break

            csv_data += table_data

        write_csv_file(output_file_path, csv_data)

    def data_url(self, company, i):
        url = 'https://www.mse.mk/mk/stats/symbolhistory/' + company.lower()
        days_in_year = 364

        current_date = datetime.now()
        offset_days = days_in_year * i
        to_date = current_date - timedelta(days=offset_days)
        from_date = to_date - timedelta(days=days_in_year)

        url += f'?FromDate={parse_string_from_date(from_date)}&ToDate={parse_string_from_date(to_date)}&Code={company}'

        return url
