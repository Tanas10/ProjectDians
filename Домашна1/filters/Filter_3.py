from filters.FilterData import *

class DataCompletenessFilter3(DataFilter):
    def apply(self, company_dates):
        print("Start filter 3")
        cpu_cores = os.cpu_count()
        data_chunks = divide_dict_into_chunks(company_dates, cpu_cores)
        with concurrent.futures.ProcessPoolExecutor(max_workers=cpu_cores) as thread_executor:
            thread_executor.map(self.handle_missing_data, data_chunks)

        return company_dates

    def handle_missing_data(self, company_dates):
        current_date = datetime.now()
        for company_name, last_transaction_date in company_dates.items():
            last_date = parse_date_from_string(last_transaction_date)
            if last_date.date() == current_date.date():
                continue

            years_since_last_transaction = current_date - last_date
            years_since_last_transaction = int(ceil(years_since_last_transaction.days / 364))
            new_data = self.fetch_new_transaction_data(company_name, last_date, years_since_last_transaction)
            if new_data is None:
                continue

            output_file_path = os.path.join('.', 'database', f'{company_name}.csv')
            existing_data = pd.read_csv(output_file_path)
            combined_data = pd.concat([new_data, existing_data], ignore_index=True)
            combined_data.to_csv(output_file_path, index=False)

    def fetch_new_transaction_data(self, company_name, last_transaction_date, years_since_last_transaction):
        columns = {
            "Date": [],
            "Price of last transaction": [],
            "Max.": [],
            "Min.": [],
            "Average price": [],
            "%prom.": [],
            "Quantity": [],
            "BEST turnover in denars": [],
            "Total turnover in denars": [],
        }
        result_df = pd.DataFrame(columns)
        session = requests.Session()
        for year_offset in range(years_since_last_transaction):
            url = self.get_url(company_name, year_offset, last_transaction_date)
            soup = fetch_soup_with_session(url, session)
            new_data_df = parse_table_to_dataframe(soup)

            if new_data_df is None:
                return result_df

            result_df.join(new_data_df)

        return result_df

    def get_url(self, company, i, last_date):
        url = 'https://www.mse.mk/mk/stats/symbolhistory/' + company.lower()
        days_in_year = 364

        current_date = datetime.now()
        offset_days = days_in_year * i
        to_date = current_date - timedelta(days=offset_days)
        from_date = to_date - timedelta(days=days_in_year)
        if from_date < last_date:
            from_date = last_date + timedelta(days=1)
        url += f'?FromDate={parse_string_from_date(from_date)}&ToDate={parse_string_from_date(to_date)}&Code={company}'
        return url
