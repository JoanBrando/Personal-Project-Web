let nextButton = document.getElementById('next');
let prevButton = document.getElementById('prev');
let backButton = document.getElementById('back');
let carrossel = document.querySelector('.carrossel');
let listHTML = document.querySelector('.carrossel .list');
let seeMoreButtons = document.querySelectorAll('.seeMore');

nextButton.onclick = function(){
    showSlider('next');
}
prevButton.onclick = function(){
    showSlider('prev');
}

let unAcceptClick;
const showSlider = (type) => {
    nextButton.style.pointerEvents = 'none';
    prevButton.style.pointerEvents = 'none';

    carrossel.classList.remove('prev', 'next');
    let items = document.querySelectorAll('.carrossel .list .item');
    if(type === 'next'){
        listHTML.appendChild(items[0]);
        carrossel.classList.add('next');
    }else{
        let positionLast = items.length -1;
        listHTML.prepend(items[positionLast]);
        carrossel.classList.add('prev');
    }

    clearTimeout(unAcceptClick);
    unAcceptClick = setTimeout(() => {
        nextButton.style.pointerEvents = 'auto';
        prevButton.style.pointerEvents = 'auto';
    }, 1000);
}

seeMoreButtons.forEach(button => {
    button.onclick = function(){
        carrossel.classList.add('showDetail');
    }
})
backButton.onclick = function(){
    carrossel.classList.remove('showDetail');
}