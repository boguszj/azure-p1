export let API_URL;

export const setApiUrl = (url) => {
    API_URL = url;
}

export const getWebsitesTimeLimits = async () => {
    const response = await fetch(API_URL + '/api/limitation');
    return await response.json();
}

export const updateWebsiteLimit = async (websiteLimit) => {
    const response = await fetch(API_URL + '/api/limitation/' + websiteLimit.id, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(websiteLimit)
    });
    return await response.json();
}

export const createWebsiteLimit = async (websiteLimit) => {
    const response = await fetch(API_URL + '/api/limitation', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(websiteLimit)
    });
    return await response.json();
}

export const deleteWebsiteLimit = async (id) => {
    const response = await fetch(API_URL + '/api/limitation/' + id, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
    });
    return await response.json();
}
