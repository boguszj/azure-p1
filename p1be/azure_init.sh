#!/bin/bash

# THIS SCRIPT IS NOT MEANT TO SERVE AS CI/CD TOOL
# PURPOSE OF THIS SCRIPT IS TO EASILY INITIALIZE PROJECT ON AZURE
# AND ALLOW TESTING AND EXPENDING

# COLOR DEFINITIONS

RED="\033[0;31m"
GREEN="\e[32m"
NC="\033[0m"

# FUNCTION DEFINITIONS

function printErrorMessage() {
  echo -e "${RED}${1}${NC}"
  exit 1
}

function printSuccessMessage() {
  echo -e "${GREEN}${1}${NC}"
}

# VARIABLE DECLARATION

if [ -z "$4" ]; then
  echo "azure-setup.sh [resource group to create] [location] [db admin username] [db admin password]"
  exit 1
fi

RG_NAME=$(echo "${1}" | tr '[:upper:]' '[:lower:]')
LOCATION=$2
DB_ADMIN_UNAME=$3
DB_ADMIN_PASS=$4

POSTGRES_NAME="${RG_NAME}db"
ALLOW_ALL_TO_POSTGRES_FW_RULE_NAME="${RG_NAME}allowalltopostgres"
ACR_NAME="${RG_NAME}acr"
STORAGE_ACCOUNT_NAME="${RG_NAME}storageaccount"
ARCHIVE_CONTAINER_NAME="${RG_NAME}archivecontainer"
APP_SERVICE_PLAN_NAME="${RG_NAME}appserviceplan"
APP_NAME="${RG_NAME}app"
APP_PORT="8080"

# ALREADY INITIALIZED DETECTION

RG_ALREADY_EXISTS=$(
  az group exists \
    --name "${RG_NAME}"
)
if [ "${RG_ALREADY_EXISTS}" = "true" ]; then
  read -p "Resource group already exists, do you want to recreate it? " -n 1 -r
  echo ""
  if [[ $REPLY =~ ^[Yy]$ ]]; then
      az group delete \
        --name "${RG_NAME}" \
        --yes \
        --only-show-errors \
        2>&1 \
        1>/dev/null
      az group wait \
        --name "${RG_NAME}" \
        --deleted \
        --only-show-errors \
        2>&1 \
        1>/dev/null
      echo "Resource group deleted"
  else
    echo "Existing..."
    exit 0
  fi
fi

# DB CREATION

echo "[1 / IDK_HOW_MANY_YET] Creating postgres server: ${POSTGRES_NAME}"

ERROR_MESSAGE=$(
  az postgres server create \
    --resource-group "${RG_NAME}" \
    --name "${POSTGRES_NAME}" \
    --location "${LOCATION}" \
    --admin-user "${DB_ADMIN_UNAME}" \
    --admin-password "${DB_ADMIN_PASS}" \
    --sku-name B_Gen5_1 \
    --only-show-errors \
    2>&1 \
    1>/dev/null
)

if [[ -z "${ERROR_MESSAGE}" ]] || [[ $ERROR_MESSAGE == *"already exists"* ]]; then
  printSuccessMessage "Postgres server created"
else
  printErrorMessage "${ERROR_MESSAGE}"
fi

# FW RULE CREATION

echo "[2 / IDK_HOW_MANY_YET] Creating firewall rule for postgres server: ${ALLOW_ALL_TO_POSTGRES_FW_RULE_NAME}"

ERROR_MESSAGE=$(
  az postgres server firewall-rule create \
    --name "${ALLOW_ALL_TO_POSTGRES_FW_RULE_NAME}" \
    --start-ip-address 0.0.0.0 \
    --end-ip-address 255.255.255.255 \
    --resource-group "${RG_NAME}" \
    --server-name "${POSTGRES_NAME}" \
    --only-show-errors \
    2>&1 \
    1>/dev/null
)

if [ -z "${ERROR_MESSAGE}" ]; then
  printSuccessMessage "FW rule created"
else
  printErrorMessage "${ERROR_MESSAGE}"
fi

# ACR CREATION

echo "[3 / IDK_HOW_MANY_YET] Creating ACR: ${ACR_NAME}"

ERROR_MESSAGE=$(
  az acr create \
    --resource-group "${RG_NAME}" \
    --name "${ACR_NAME}" \
    --sku Basic \
    --only-show-errors \
    2>&1 \
    1>/dev/null
)

if [ -z "${ERROR_MESSAGE}" ]; then
  printSuccessMessage "ACR created"
else
  printErrorMessage "${ERROR_MESSAGE}"
fi

# STORAGE ACCOUNT CREATION

echo "[4 / IDK_HOW_MANY_YET] Creating storage account: ${STORAGE_ACCOUNT_NAME}"

ERROR_MESSAGE=$(
  az storage account create \
    --name "${STORAGE_ACCOUNT_NAME}" \
    --resource-group "${RG_NAME}" \
    --location "${LOCATION}" \
    --only-show-errors \
    2>&1 \
    1>/dev/null
)

if [ -z "${ERROR_MESSAGE}" ]; then
  printSuccessMessage "Storage account created"
else
  printErrorMessage "${ERROR_MESSAGE}"
fi

# STORAGE CONTAINER CREATION

echo "[5 / IDK_HOW_MANY_YET] Creating storage container: ${ARCHIVE_CONTAINER_NAME}"

ERROR_MESSAGE=$(
  az storage container create \
    --name "${ARCHIVE_CONTAINER_NAME}" \
    --account-name "${STORAGE_ACCOUNT_NAME}" \
    --resource-group "${RG_NAME}" \
    --only-show-errors \
    2>&1 \
    1>/dev/null
)

if [ -z "${ERROR_MESSAGE}" ]; then
  printSuccessMessage "Storage container created"
else
  printErrorMessage "${ERROR_MESSAGE}"
fi

# BUILD JAR

echo "[6 / IDK_HOW_MANY_YET] Building jar"

OUTPUT=$(mvn clean install)

if [[ $OUTPUT == *"[INFO] BUILD SUCCESS"* ]]; then
  printSuccessMessage "JAR built"
else
  printErrorMessage "${ERROR_MESSAGE}"
fi

# BUILDING DOCKER IMAGE

echo "[7 / IDK_HOW_MANY_YET] Building docker image"

IMAGE_REPOSITORY_URL="${ACR_NAME}.azurecr.io"
IMAGE_ADDRESS="${IMAGE_REPOSITORY_URL}/p1be"
CONNECTION_STRING=$(az storage account show-connection-string --name ${STORAGE_ACCOUNT_NAME} --resource-group ${RG_NAME} | jq -r '.connectionString')
DB_URL="jdbc:postgresql://${POSTGRES_NAME}.postgres.database.azure.com:5432/postgres"
DB_USERNAME="${DB_ADMIN_UNAME}@${POSTGRES_NAME}"

ERROR_MESSAGE=$(
  DOCKER_BUILDKIT=0 docker build . \
    -t "${IMAGE_ADDRESS}:latest" \
    -t "${IMAGE_ADDRESS}:1.0.0" \
    --build-arg SERVER_PORT="${APP_PORT}" \
    --build-arg SPRING_DATASOURCE_URL="${DB_URL}" \
    --build-arg SPRING_DATASOURCE_USERNAME="${DB_USERNAME}" \
    --build-arg SPRING_DATASOURCE_PASSWORD="${DB_ADMIN_PASS}" \
    --build-arg APP_ARCHIVIZATION_CONTAINERNAME="${ARCHIVE_CONTAINER_NAME}" \
    --build-arg APP_STORAGE_AZURECONNECTIONSTRING="${CONNECTION_STRING}" \
    --no-cache \
    2>&1 \
    1>/dev/null
)

if [ -z "${ERROR_MESSAGE}" ]; then
  printSuccessMessage "Docker image built"
else
  printErrorMessage "${ERROR_MESSAGE}"
fi

# PUSHING TO ACR

echo "[8 / IDK_HOW_MANY_YET] Pushing docker image to ACR: ${IMAGE_ADDRESS}"

ERROR_MESSAGE=$(
  az acr login --name "${ACR_NAME}" 2>&1 1>/dev/null && docker push "${IMAGE_ADDRESS}" 2>&1 1>/dev/null
)

if [ -z "${ERROR_MESSAGE}" ]; then
  printSuccessMessage "Docker image pushed to ACR"
else
  printErrorMessage "${ERROR_MESSAGE}"
fi

# CREATE APP SERVICE PLAN

echo "[9 / IDK_HOW_MANY_YET] Creating app service plan: ${APP_SERVICE_PLAN_NAME}"

ERROR_MESSAGE=$(
  az appservice plan create \
    --name "${APP_SERVICE_PLAN_NAME}" \
    --resource-group "${RG_NAME}" \
    --is-linux \
    --only-show-errors \
    2>&1 \
    1>/dev/null
)

if [ -z "${ERROR_MESSAGE}" ]; then
  printSuccessMessage "App service plan created"
else
  printErrorMessage "${ERROR_MESSAGE}"
fi

# CREATE APP

echo "[10 / IDK_HOW_MANY_YET] Creating app: ${APP_NAME}"

ERROR_MESSAGE=$(
  az webapp create \
    --resource-group "${RG_NAME}" \
    --plan "${APP_SERVICE_PLAN_NAME}" \
    --name "${APP_NAME}" \
    --deployment-container-image-name "${IMAGE_ADDRESS}:latest" \
    --only-show-errors \
    2>&1 \
    1>/dev/null
)

if [ -z "${ERROR_MESSAGE}" ]; then
  printSuccessMessage "App created"
else
  printErrorMessage "${ERROR_MESSAGE}"
fi

# OPENING PORT

echo "[11 / IDK_HOW_MANY_YET] Opening port: ${APP_PORT}"

ERROR_MESSAGE=$(
  az webapp config appsettings set \
    --resource-group "${RG_NAME}" \
    --name "${APP_NAME}" \
    --settings WEBSITES_PORT="${APP_PORT}" \
    --only-show-errors \
    2>&1 \
    1>/dev/null
)

if [ -z "${ERROR_MESSAGE}" ]; then
  printSuccessMessage "Port opened"
else
  printErrorMessage "${ERROR_MESSAGE}"
fi

# ENABLE WEB APP IDENTITY

echo "[12 / IDK_HOW_MANY_YET] Enabling web app identity"

PRINCIPAL_ID=$(
  az webapp identity assign \
    --resource-group "${RG_NAME}" \
    --name "${APP_NAME}" \
    --query principalId \
    --output tsv
)
SUBSCRIPTION_ID=$(
  az account show \
    --query id \
    --output tsv
)

sleep 120

ERROR_MESSAGE=$(
  az role assignment create \
    --assignee "${PRINCIPAL_ID}" \
    --scope "/subscriptions/${SUBSCRIPTION_ID}/resourceGroups/${RG_NAME}/providers/Microsoft.ContainerRegistry/registries/${ACR_NAME}" \
    --role "AcrPull" \
    --only-show-errors \
    2>&1 \
    1>/dev/null
)

if [ -z "${ERROR_MESSAGE}" ]; then
  printSuccessMessage "Identity enabled"
else
  printErrorMessage "${ERROR_MESSAGE}"
fi

# ASSIGN IDENTITY TO APP

echo "[13 / IDK_HOW_MANY_YET] Assigning permissions web app identity"

ERROR_MESSAGE=$(
  az resource update \
    --ids "/subscriptions/${SUBSCRIPTION_ID}/resourceGroups/${RG_NAME}/providers/Microsoft.Web/sites/${APP_NAME}/config/web" \
    --set "properties.acrUseManagedIdentityCreds=True" \
    --only-show-errors \
    2>&1 \
    1>/dev/null
)

if [ -z "${ERROR_MESSAGE}" ]; then
  printSuccessMessage "Identity enabled"
else
  printErrorMessage "${ERROR_MESSAGE}"
fi

# START APP

echo "[14 / IDK_HOW_MANY_YET] Starting app"

ERROR_MESSAGE=$(
  az webapp config container set \
    --name "${APP_NAME}" \
    --resource-group "${RG_NAME}" \
    --docker-custom-image-name "${IMAGE_ADDRESS}" \
    --docker-registry-server-url "https://${IMAGE_REPOSITORY_URL}" \
    --only-show-errors \
    2>&1 \
    1>/dev/null
)

if [ -z "${ERROR_MESSAGE}" ]; then
  printSuccessMessage "App starting"
else
  printErrorMessage "${ERROR_MESSAGE}"
fi

# ENABLE LOGGING

echo "[15 / IDK_HOW_MANY_YET] Enabling logging"

ERROR_MESSAGE=$(
  az webapp log config \
    --name "${APP_NAME}" \
    --resource-group "${RG_NAME}" \
    --docker-container-logging filesystem
)