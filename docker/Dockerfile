FROM wssy001/ubuntu:focal
LABEL authors="Tyler"

ADD fallout76-assistant.tar.gz /root
RUN cd /root \
    && tar -C /root/fallout76-assistant -xzvf fallout76-assistant.tar.gz \
    && rm -rf fallout76-assistant.tar.gz

WORKDIR /root/fallout76-assistant
CMD ["/root/fallout76-assistant/fallout76-assistant-robot"]