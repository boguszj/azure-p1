chrome.storage.sync.get('currentlyBlockedUrl', ({ currentlyBlockedUrl }) => {
    if (currentlyBlockedUrl) {
        document.getElementById('site-name').innerText = currentlyBlockedUrl;
    }
});
