import {toMinutes, toSeconds} from "./popup-utils.js";
import {getNameErrorMessage, getTimeErrorMessage, clearErrorMessages} from "./validation-utils.js";
import {getWebsitesTimeLimits, updateWebsiteLimit, createWebsiteLimit, deleteWebsiteLimit} from "./http-utils.js";
import { displayWebsitesLimitsList, insertErrorMessage } from "./dom-updater.js";

let editedWebsiteId = null;
const PERIOD_SECONDS = 86400;

const websiteForm = document.getElementById("website-form");
websiteForm.addEventListener('submit', (event) => {
    onFormSubmitted(event)
});
document.getElementById("cancel-button").addEventListener('click', () => {clearAndHideForm()});
document.getElementById("add-button").addEventListener('click', () => { onAddClicked()});

const nameInput = document.getElementById("name");
const timeInput = document.getElementById("time");
const actionName = document.getElementById("action-name");

const onUpdateWebsiteLimitClicked = (websiteLimit) => {
    actionName.innerText = 'Edytuj ograniczenie';
    editedWebsiteId = websiteLimit.id;
    nameInput.value = websiteLimit.domain;
    timeInput.value = toMinutes(websiteLimit.limitationSeconds);
    clearErrorMessages();
    websiteForm.classList.remove("display-none");
}

const onWebsiteLimitDeleted = (id) => {
    deleteWebsiteLimit(id).then((response) => {
        displayWebsitesLimitsList(response, onUpdateWebsiteLimitClicked, onWebsiteLimitDeleted);
    })
}

const clearAndHideForm = () => {
    clearFormFields();
    websiteForm.classList.add("display-none");
}

const clearFormFields = () => {
    nameInput.value = '';
    timeInput.value = '';
}

const onAddClicked = () => {
    actionName.innerText = 'Dodaj ograniczenie';
    editedWebsiteId = null;
    clearErrorMessages();
    clearFormFields();
    websiteForm.classList.remove("display-none");
}

const onFormSubmitted = async (event) => {
    event.preventDefault();
    clearErrorMessages();
    const nameValidationMessage = getNameErrorMessage(nameInput.value);
    const timeValidationMessage = getTimeErrorMessage(timeInput.value);

    if (nameValidationMessage) {
        insertErrorMessage(nameInput, nameValidationMessage);
    }
    if (timeValidationMessage) {
        insertErrorMessage(timeInput, timeValidationMessage);
    }

    if (!nameValidationMessage && !timeValidationMessage) {
        let response;
        if (editedWebsiteId) {
            response = await updateWebsiteLimit({id: editedWebsiteId, domain: nameInput.value, limitationSeconds: toSeconds(timeInput.value), periodSeconds: PERIOD_SECONDS})
        } else {
            response = await createWebsiteLimit({domain: nameInput.value, limitationSeconds: toSeconds(timeInput.value), periodSeconds: PERIOD_SECONDS})
        }
        displayWebsitesLimitsList(response, onUpdateWebsiteLimitClicked, onWebsiteLimitDeleted);
        clearAndHideForm();
    }
}


getWebsitesTimeLimits().then((res) => {
    displayWebsitesLimitsList(res, onUpdateWebsiteLimitClicked, onWebsiteLimitDeleted);
});


