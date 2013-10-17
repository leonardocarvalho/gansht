import pymongo
import pymongo.uri_parser
import pymongo.errors

db=None

def setup(app):
    global db
    uri = app.config["MONGO_HOST"]
    try:
        db_name = pymongo.uri_parser.parse_uri(uri)["database"]
    except pymongo.errors.InvalidURI:
        db_name = app.config["MONGO_DBNAME"]
    db = pymongo.MongoClient(app.config["MONGO_HOST"], auto_start_request=True)[db_name]
