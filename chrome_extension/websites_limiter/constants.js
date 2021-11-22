const isDev = true; // set to false when backend runs on azure app service
const APP_SERVICE_URL = ''; // set created azure app service url if not developing locally
export const API_URL = isDev ? 'http://localhost:8080': APP_SERVICE_URL;
