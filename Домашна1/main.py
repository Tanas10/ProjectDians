from Домашна1.filters.Filter_1 import *
from Домашна1.pipelines.PipeLine import *

if __name__ == '__main__':

    print("Старт")
    time_now = time.time()
    pipeline = ScrapingPipeline()
    filtered_data = pipeline.execute([])
    time_taken = time.time() - time_now
    print("Апликацијата завши во {:.2f} секунди".format(time_taken))
