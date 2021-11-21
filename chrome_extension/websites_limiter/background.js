const isDev = true;
const API_URL = isDev ? 'http://localhost:8080' : '';

const handleBlockedWebsites = async () => {
    getCurrentHttpTab(async (tab) => {
        const blockedWebsites = await getBlockedWebsites(tab.url);
        redirectIfWebsiteBlocked(blockedWebsites);
    });
}

const getCurrentHttpTab= (callback) => {
    let queryOptions = {active: true, currentWindow: true};
    chrome.tabs.query(queryOptions, async (tabs) => {
        if (tabs[0] && tabs[0].url.startsWith('http')) {
            callback(tabs[0]);
        }
    });
}

const redirectIfWebsiteBlocked = (blockedWebsites) => {
    getCurrentHttpTab(async (tab) => {
        blockedWebsites.forEach(website => {
            if (getHostFromUrl(tab.url) === website) {
                chrome.storage.sync.set({currentlyBlockedUrl: website});
                chrome.tabs.update(tab.id, {url: 'limit_info/limit_info.html'});
            }
        })
    });
}


const getBlockedWebsites = async (url) => {
    const allWebsites = await getAllWebsites(url);
    return Object.entries(allWebsites)
        .filter(el => el[1].limitationSeconds < el[1].usedSeconds)
        .map(el => el[1].domain);
}

const getAllWebsites = async (url) => {
    const body = {url: url, intervalSeconds: 5};
    const response = await fetch(API_URL + '/api/report', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
    });
    return await response.json();
}

const getHostFromUrl = (url) => {
    return new RegExp("(:?[a-zA-Z0-9_]+\\.)*((?:[a-zA-Z0-9_]+)\\.(?:[a-zA-Z0-9_]+))").exec(url)[2];
}

setInterval(() => {
    //send request
    handleBlockedWebsites();
}, 5000)
