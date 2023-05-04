FROM wssy001/ubuntu:focal
LABEL authors="Tyler"

COPY fallout76-assistant.tar.gz /root/fallout76-assistant/
RUN cd /root/fallout76-assistant \
    && tar -xzvf /root/fallout76-assistant/fallout76-assistant.tar.gz \
    && rm -rf /root/fallout76-assistant/fallout76-assistant.tar.gz

WORKDIR /root/fallout76-assistant
CMD ["/root/fallout76-assistant/fallout76-assistant-robot"]