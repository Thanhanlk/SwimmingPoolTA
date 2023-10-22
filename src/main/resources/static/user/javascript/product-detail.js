const addCartFormDOM = document.getElementById('add-cart-form');
(function main(){
    addCartFormDOM.addEventListener('submit', event => {
        const checkox = addCartFormDOM.querySelectorAll('input[type=checkbox]');
        if (checkox.length == 0 || Array.from(checkox).every(x => !x.checked)) {
            event.preventDefault();
        }
    })
})()