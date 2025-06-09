import time
import requests as req

API_URL = 'https://api.openalex.org/'
CHUNK_SIZE = 50


def _direct_url(id_url) -> req.Response:
    api_url = id_url.replace("https://openalex.org/",
                             "https://api.openalex.org/")
    response = req.get(
        api_url, params={"mailto": "matheus.lazaro@outlook.com"})
    response.raise_for_status()
    return response.json()


def _get_params(filter, cursor=None):
    param = {
        'per-page': 200,
        'mailto': 'matheus.lazaro@outlook.com'
    }
    if filter:
        param['filter'] = filter
    if cursor:
        param['cursor'] = cursor
    return param


def fetch_data(endpoint=None, filter=None, cursor=None, id_url=None):
    time.sleep(0.5)
    if id_url:
        return _direct_url(id_url)
    response = req.get(API_URL + endpoint, params=_get_params(filter, cursor))
    response.raise_for_status()
    return response.json()


def get_chunk_list(lst, chunk_size=CHUNK_SIZE):
    for i in range(0, len(lst), chunk_size):
        yield lst[i:i + chunk_size]
