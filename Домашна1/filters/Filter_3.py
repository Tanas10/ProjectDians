from Домашна1.filters.FilterData import *

class DataCompletenessDataFilter(DataFilter):
    def apply(self, company_dates):
        print("Старт Филтер 3")
        now = datetime.now()
        for company, last_date_str in company_dates.items():
            last_date = parse_date_from_string(last_date_str)
            if last_date.date() == now.date():
                continue
            months_since_last_update = now - last_date
            months_since_last_update = int(ceil(months_since_last_update.days / 364))
            new_data = self.fetch_new_data(company, last_date, months_since_last_update)
            if new_data is None:
                continue
            output_dir = os.path.join('.', 'database', f'{company}.xlsx')
            existing_data = pd.read_excel(output_dir)
            combined_data = pd.concat([new_data, existing_data], ignore_index=True)
            combined_data.to_excel(output_dir, index=False)
        return company_dates

    def fetch_new_data(self, company, last_date, years_to_fetch):
        for i in range(years_to_fetch):
            url = self.build_url(company, i, last_date)
            soup = fetch_soup_from_url(url)
            fetched_data = extract_table_data(soup)
        return fetched_data

    def build_url(self, company, i, last_date):
        url = 'https://www.mse.mk/mk/stats/symbolhistory/' + company.lower()
        days = 364

        now = datetime.now()
        days_to_subtract = days * i
        to_date = now - timedelta(days=days_to_subtract)
        from_date = to_date - timedelta(days=days)

        if from_date < last_date:
            from_date = last_date + timedelta(days=1)

        url += f'?FromDate={parse_string_from_date(from_date)}&ToDate={parse_string_from_date(to_date)}&Code={company}'

        return url