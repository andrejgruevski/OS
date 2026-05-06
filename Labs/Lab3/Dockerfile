FROM debian:12-slim
RUN apt-get update && apt-get install -y python3-minimal
RUN mkdir -p /srv/catalog
CMD ["python3", "-m", "http.server", "9000", "--directory", "/srv/catalog"]