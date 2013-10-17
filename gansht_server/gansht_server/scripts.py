from gansht_server.models import user
from gansht_server.models import repository


def add_user(username, password):
    repository.user.save_user(user.User(username, password))
