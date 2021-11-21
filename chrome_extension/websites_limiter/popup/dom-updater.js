
export const displayWebsitesLimitsList = (websitesLimits, updateCallback, deleteCallback) => {
    const websiteList = document.getElementById("websites-list");
    websiteList.innerHTML = '';
    if (websitesLimits.length === 0) {
        websiteList.appendChild(createEmptyListMessage());
    }
    websitesLimits.forEach(website => {
        websiteList.appendChild(createListItem(website, updateCallback, deleteCallback));
    });
}

const createEmptyListMessage = () => {
    const messageElement = document.createElement("small");
    messageElement.classList.add("m-left-15");
    messageElement.innerText = "Brak zdefiniowanych ogarniczeń";
    return messageElement;
}

const createListItem = (websiteLimit, updateCallback, deleteCallback) => {
    const listItem = document.createElement("li");
    listItem.id = websiteLimit.id;
    listItem.classList.add("cursor-auto");
    listItem.classList.add("list-item");
    listItem.classList.add("mdc-list-item");

    const name = document.createElement("span");
    name.classList.add("mdc-list-item__text");
    name.innerText = websiteLimit.domain;

    const time = document.createElement("span");
    time.classList.add("mdc-list-item__text");
    time.innerText = Math.round(websiteLimit.limitationSeconds / 60) + 'min';

    const editIcon = document.createElement("span");
    editIcon.classList.add("material-icons");
    editIcon.classList.add("action-icon")
    editIcon.innerText = 'edit';
    editIcon.addEventListener('click', () => {
        updateCallback(websiteLimit);
    })

    const deleteIcon = document.createElement("span");
    deleteIcon.classList.add("material-icons");
    deleteIcon.classList.add("action-icon")
    deleteIcon.innerText = 'delete';
    deleteIcon.addEventListener('click', () => {
        deleteCallback(websiteLimit.id);
    });

    listItem.appendChild(name);
    listItem.appendChild(time);
    listItem.appendChild(editIcon);
    listItem.appendChild(deleteIcon);

    return listItem;
}

export const insertErrorMessage = (inputNode, message) => {
    const errorMessage = document.createElement("p");
    errorMessage.classList.add("error-message");
    errorMessage.innerText = message;
    insertAfter(errorMessage, inputNode.parentNode);
}

const insertAfter = (newNode, referenceNode) => {
    referenceNode.parentNode.insertBefore(newNode, referenceNode.nextSibling);
}
