from cryptacular import bcrypt


class User(object):

    _pwd_manager = bcrypt.BCRYPTPasswordManager()

    def __init__(self, username, password=None, _id=None):
        self._id = _id
        self.username = username
        if password:
            self.set_password(password)

    def set_password(self, password):
        self._password = self._pwd_manager.encode(password)

    def has_password(self, password):
        return self._pwd_manager.check(self._password, password)

    def serialize(self):
        return {
            "username":  self.username,
            "password": self._password,
        }

    @classmethod
    def load(cls, data):
        user = cls(data["username"], _id=data.get("_id"))
        user._password = data["password"]
        return user
