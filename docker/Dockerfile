FROM wssy001/ubuntu:focal
LABEL authors="Tyler"

COPY *.tar.gz /usr/fallout76-assistant/
RUN cd /usr/fallout76-assistant \
    && tar -xzvf /usr/fallout76-assistant/*.tar.gz \
    && rm -rf /usr/fallout76-assistant/*.tar.gz \
    && chmod a+rw /usr/fallout76-assistant/* \
    && mkdir -p /usr/fallout76-assistant/lib \
    && mv /usr/fallout76-assistant/*.so /usr/fallout76-assistant/lib

WORKDIR /usr/fallout76-assistant
CMD ["/usr/fallout76-assistant/fallout76-assistant-robot","-Xmx512m","-XX:+PrintGC","-XX:+PrintGCTimes","-XX:+VerboseGC","-Djava.home=.","-Djavax.xml.accessExternalDTD=all"]