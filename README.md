# azure-p1

## 1. Goal of the project

The goal of the project is to build a system that allows its users to controll time they are spending on any given website.
It was motivated by ever increasing addicton to social media.
The system should allow to set limit of time spent on any given website and block access to it after the limit is exceeded.

## 2. Main components

- Backend service:
  - receiving reports of currently active website in users browser
  - receiving limits of times users want to spend on websites
  - returning informations about time limits usage
  - archiving collected data for the purpose of teaching potential AI in the future
- Chrome extension serving as a client to the service:
  - reporting time spent on websites
  - blocking access to websites after given time periods

## 3. Infrastucture

<image to be embedded>

## 4. Setup

### 4.1 Infrastructure setup

Run script ``/p1be/azure_init.sh``.

**Note** Depending on shell being used and (on Windows WSL) installation of ``azure-cli `` ([issue](https://github.com/Azure/azure-cli/issues/15745)
carriage returns may not be handled correctly. If so, step 12. is bound to fail. If this happens steps 12 through 16 can still be executed manually.

### 4.2 Chrome extension setup

## 5. Presentation

<Youtube video to be embedded>

## 6. Potential AI usages

To avoid performance issues related to big volumen of data being stored in PostgresDB all reports are being archived after specified time period
(default retention period is set to 48h) and saved in JSON format in storage container. From there data may serve as input for machine learning:

- content recommendation engine based on browsing history and attension span
- dangerous search patterns recognition
- ...
