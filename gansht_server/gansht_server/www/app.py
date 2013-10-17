import flask

import gansht_server.config as config
import gansht_server.mongo as mongo
import gansht_server.www.photo as www_photo
import gansht_server.www.results as www_results


def create_app():
    app = flask.Flask(__name__)
    app.config.from_object(config.config)

    app.register_blueprint(www_photo.blueprint)
    app.register_blueprint(www_results.blueprint)

    return app


app = create_app()
mongo.setup(app)


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=config.config.PORT, use_reloader=config.config.USE_RELOADER)
