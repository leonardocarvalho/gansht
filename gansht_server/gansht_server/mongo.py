import pymongo.connection as pymongo

db=None

def setup(app):
    global db
    db = pymongo.Connection("localhost", safe=True, tz_aware=False)[app.config["MONGO_DBNAME"]]
