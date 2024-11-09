from math import ceil
import openpyxl
import pandas as pd
import requests
from bs4 import BeautifulSoup
import concurrent.futures
import time
from datetime import datetime, timedelta
import os
from datetime import datetime
import re
class DataFilter:
    def apply(self, data):
        raise NotImplementedError("Each filter must implement!")

def data_file_exists(company_name):
    path = os.path.join('.', 'database', f'{company_name}.xlsx')
    if os.path.exists(path):
        return True
    return False

def extract_table_data(soup):
    results_table = soup.select_one('#resultsTable')
    if results_table is None:
        return None
    table_rows = results_table.select('tbody > tr')

    data_dict = {
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

    for row in table_rows:
        cells = row.select('td')

        data_dict['Датум'].append(cells[0].text)
        data_dict['Цена на последна трансакција'].append(cells[1].text)
        data_dict['Мак.'].append(cells[2].text)
        data_dict['Мин.'].append(cells[3].text)
        data_dict['Просечна цена'].append(cells[4].text)
        data_dict['%пром.'].append(cells[5].text)
        data_dict['Количина'].append(cells[6].text)
        data_dict['Промет во БЕСТ во денари'].append(cells[7].text)
        data_dict['Вкупен промет во денари'].append(cells[8].text)
    return pd.DataFrame(data_dict)

def fetch_soup_from_url(url):
    response = requests.get(url)
    return BeautifulSoup(response.text, 'html.parser')

def parse_string_from_date(date_obj):
    formatted_date = date_obj.__str__().split(' ')[0].split("-")
    return formatted_date[2] + "." + formatted_date[1] + "." + formatted_date[0]
def parse_date_from_string(date_string):
    date_format = "%d.%m.%Y"
    return datetime.strptime(date_string, date_format)
