FROM jboss/wildfly
ENV SETUP_DIR /opt/jboss/wildfly/setup
ADD setup $SETUP_DIR
COPY target/wprof.war $SETUP_DIR
CMD ["/opt/jboss/wildfly/setup/setup.sh"]

