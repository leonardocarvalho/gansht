import pymongo.connection as pymongo

db=None

def setup(app):
    global db
    db = pymongo.MongoClient(app.config["MONGO_HOST"],
                             auto_start_request=True)[app.config["MONGO_DBNAME"]]
