export const getNameErrorMessage = (nameValue) => {
    const urlValidator = new RegExp("^[a-zA-Z0-9][a-zA-Z0-9-]{1,61}[a-zA-Z0-9]\\.[a-zA-Z]{2,6}$");
    if (nameValue.trim().length === 0) {
        return "To pole jest wymagane";
    } else if (!nameValue.match(urlValidator)) {
        return "Podaj prawidłowy adres url";
    } else {
        return null;
    }
}

export const getTimeErrorMessage = (timeValue) => {
    if (timeValue.trim().length === 0) {
        return "To pole jest wymagane";
    } else if (isNaN(timeValue) || timeValue < 0) {
        return "Wartość pola musi być liczbą dodatnią";
    } else {
        return null;
    }
}

export const clearErrorMessages = () => {
    const errorMessages = document.querySelectorAll('.error-message');
    errorMessages.forEach(error => error.remove());
}
