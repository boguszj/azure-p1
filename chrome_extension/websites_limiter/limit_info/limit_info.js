chrome.storage.sync.get('currentlyBlockedUrl', ({ currentlyBlockedUrl }) => {
    console.log(currentlyBlockedUrl);
    if (currentlyBlockedUrl) {
        document.getElementById('site-name').innerText = currentlyBlockedUrl;
    }
});
