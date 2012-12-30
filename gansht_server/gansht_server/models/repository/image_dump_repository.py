from gansht_server import mongo


class ImageDumpRepository(object):

    def save(self, data):
        mongo.db.image_dump.insert(data)


it = ImageDumpRepository()
