from filters.Filter_1 import *
from filters.Filter_2 import *
from filters.Filter_3 import *

class ScrapingPipeline:
    def __init__(self):
        self.filters = []
        self.filters.append(Filter1CodeDownload())
        self.filters.append(Filter2DataSaver())
        self.filters.append(DataCompletenessDataFilter())

    def add_filter(self, filter):
        self.filters.append(filter)

    def execute(self, data):
        for filter in self.filters:
            data = filter.apply(data)
        return data