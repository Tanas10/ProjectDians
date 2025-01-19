from math import ceil
import pandas as pd
import csv
import requests
from bs4 import BeautifulSoup
from multiprocessing import Pool, Manager
import concurrent.futures
import time
from datetime import datetime, timedelta
import os
from datetime import datetime
import re
import openpyxl


def parse_date_from_string(date_string):
    date_format = "%d.%m.%Y"
    return datetime.strptime(date_string, date_format)


def parse_string_from_date(date_obj):
    tmp = date_obj.__str__().split(' ')[0].split("-")
    return tmp[2] + "." + tmp[1] + "." + tmp[0]

def write_csv_file(file_path, dataset):
    with open(file_path, 'w', newline='') as csv_file:
        csv_writer = csv.writer(csv_file)
        csv_writer.writerows(dataset)

def extract_table_data_from_soup(soup):
    table_element = soup.select_one('#resultsTable')
    if table_element is None:
        return None
    table_rows = table_element.select('tbody > tr')

    extracted_data = []
    for row in table_rows:
        cell_data = []
        cells = row.select('td')
        for td in cells:
            cell_data.append(td.text)
        extracted_data.append(cell_data)

    return extracted_data

def parse_table_to_dataframe(soup):
    table_element = soup.select_one('#resultsTable')
    if table_element is None:
        return None
    table_rows = table_element.select('tbody > tr')

    data_dict = {
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

    df_result = pd.DataFrame(data_dict)
    return df_result

def fetch_soup_from_url(url):
    response = requests.get(url)
    return BeautifulSoup(response.text, 'html.parser')

def fetch_soup_with_session(url, session):
    response = session.get(url)
    return BeautifulSoup(response.text, 'html.parser')


def data_file_exists(company_name):
    path = os.path.join('.', 'database', f'{company_name}.csv')
    if os.path.exists(path):
        return True
    return False


def divide_dict_into_chunks(source_dict, num_chunks):
    items_list = list(source_dict.items())
    chunk_size = len(items_list) // num_chunks
    extra_items = len(items_list) % num_chunks

    chunked_dicts = []
    start_index = 0
    for i in range(num_chunks):
        end_index = start_index + chunk_size + (1 if i < extra_items else 0)
        chunked_dicts.append(dict(items_list[start_index:end_index]))
        start_index = end_index

    return chunked_dicts

class DataFilter:
    def apply(self, data):
        raise NotImplementedError("Each filter must implement!")
