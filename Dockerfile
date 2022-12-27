FROM ubuntu:focal
RUN mkdir -p /home/fallout76-assistant
EXPOSE 35701/tcp
ADD ./fallout76-assistant /home/fallout76-assistant
WORKDIR /home/fallout76-assistant
ENTRYPOINT ["/home/fallout76-assistant/run.sh"]