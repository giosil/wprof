#!/bin/bash

echo "=> Executing setup script"

cp "$SETUP_DIR/wprof.sh" "$HOME/wprof.sh"

# echo "=> Install job in crontab"
# 
# crontab -l > curr_crontab
# 
# if grep -q wprof.sh curr_crontab; then
#   echo "Job already installed."
# else
#   echo "*/1 * * * * $HOME/wprof.sh > /dev/null 2>&1" >> curr_crontab
#   crontab curr_crontab
# fi
# 
# rm curr_crontab

JBOSS_HOME=/opt/jboss/wildfly
JBOSS_CLI=$JBOSS_HOME/bin/jboss-cli.sh
SETUP_DIR=$JBOSS_HOME/setup

function wait_for_server() {
  until `$JBOSS_CLI -c ":read-attribute(name=server-state)" 2> /dev/null | grep -q running`; do
    sleep 1
  done
}

# Starting WildFly server in background
echo "=> Starting WildFly server"
echo "JBOSS_HOME: " $JBOSS_HOME
echo "JBOSS_CLI : " $JBOSS_CLI
echo "SETUP_DIR : " $SETUP_DIR

$JBOSS_HOME/bin/standalone.sh -b 0.0.0.0 &

echo "=> Waiting for the server to boot"
wait_for_server

# Deploy application 
echo "=> Deploy application"
$JBOSS_CLI -c --command="deploy $SETUP_DIR/wprof.war --force"

# Shutting down 
echo "=> Shutting down WildFly"
$JBOSS_CLI -c --command="shutdown"

# Restarting WildFly server in foreground
echo "=> Restarting WildFly"
$JBOSS_HOME/bin/standalone.sh -b 0.0.0.0
