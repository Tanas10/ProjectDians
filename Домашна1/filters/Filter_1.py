from filters.FilterData import *

class Filter1CodeDownload(DataFilter):
    def apply(self, date):
        print("Start filter 1")

        url = 'https://www.mse.mk/mk/stats/symbolhistory/kmb'
        html = fetch_soup_from_url(url)

        dropdown = html.select_one('#Code')
        options_list = dropdown.select('option')
        for option in options_list:
            if not re.search(r'\d', option.text):
                date.append(option.text)

        return date
