"use-strict"
const checkboxCartSelector = document.getElementById('cart-selector');
const checkboxCartIds = document.querySelectorAll('input[type=checkbox][name=cartId]');
const originalPriceDOM = document.getElementById('original-price');
const discountDOM = document.getElementById('discount-price');
const totalPriceDOM = document.getElementById('total-price');
const quantityCartDOM = document.getElementById('quantity-cart');
const btnCheckoutDOM = document.getElementById('btn-checkout');

const checkboxCartIdsArr = Array.from(checkboxCartIds);

function countChecked() {
    return getCheckedCart().length;
}

function getCheckedCart() {
    return checkboxCartIdsArr.filter(element => element.checked);
}

function calculatePrice() {
    const checkedCart = getCheckedCart();
    const { newPrice, oldPrice } = checkedCart
        .map(cart => cart.dataset)
        .reduce(({ newPrice, oldPrice }, { price, discount }) => (
            { newPrice:  newPrice + (+price * ((100 - discount) / 100)), oldPrice: oldPrice + (+price) }
        ), { newPrice: 0, oldPrice: 0 })
    originalPriceDOM.textContent = $formatter.format(oldPrice);
    discountDOM.textContent = $formatter.format(oldPrice - newPrice);
    totalPriceDOM.textContent = $formatter.format(newPrice);
    quantityCartDOM.textContent = `${ checkedCart.length }`;
}

function setHrefBtnCheckout(checked) {
    if (checked) {
        btnCheckoutDOM.formAction = btnCheckoutDOM.dataset.href;
        btnCheckoutDOM.classList.remove('disabled');
    } else {
        btnCheckoutDOM.formAction = 'javascript:void(0)';
        btnCheckoutDOM.classList.add('disabled');
    }

}

(function main() {
    checkboxCartSelector.addEventListener('change', e => {
        setHrefBtnCheckout(e.target.checked && checkboxCartIds.length > 0);
        checkboxCartIds.forEach(element => {
            element.checked = e.target.checked;
        });
        calculatePrice();
    })

    checkboxCartIds.forEach(element => {
        element.addEventListener('change', e => {
            const checked = countChecked();
            const isCheckedAll = checked === checkboxCartIdsArr.length;
            checkboxCartSelector.checked = e.target.checked && isCheckedAll;
            calculatePrice();
            setHrefBtnCheckout(checked > 0);
        })
    })
})()