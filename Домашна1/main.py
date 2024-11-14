from filters.Filter_1 import *
from pipelines.PipeLine import *

if __name__ == '__main__':

    print("Start")
    time_now = time.time()
    url = 'https://www.mse.mk/mk/stats/symbolhistory/kmb'
    pipeline = ScrapingPipeline()
    filtered_data = pipeline.execute([])
    time_taken = time.time() - time_now
    print("Potrebno vreme {:.2f} sekundi".format(time_taken))
