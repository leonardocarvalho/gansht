import datetime


class Config(object):
    SECRET_KEY = "po4[23r[ofj431!!$"
    MONGO_DBNAME = "gansht"
    MONGO_HOST = "localhost"
    PERMANENT_SESSION_LIFETIME = datetime.timedelta(days=7)

    PORT = 5050
    TESTING = False
    DEBUG = True
    USE_RELOADER = True


config = Config()
