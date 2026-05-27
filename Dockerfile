FROM tomcat:9.0-jdk17

COPY earn.war /usr/local/tomcat/webapps/ROOT.war

ENV PORT=8080

EXPOSE 8080

CMD ["catalina.sh", "run"]