import requests as req

API_URL = 'https://api.openalex.org/'


def fetch_data(endpoint, params=None):
    if params is None:
        params = {}
    response = req.get(API_URL + endpoint, params=params)
    response.raise_for_status()
    return response.json()
