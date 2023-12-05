const bodyChatGpt = document.querySelector("#chatGPT .card-body");

// in chat window: scroll to end conversation when window is shown
$('#chatGPT').on('shown.bs.collapse', function () {
    bodyChatGpt.scrollTo({
        behavior: 'smooth',
        top: bodyChatGpt.scrollHeight
    })
})