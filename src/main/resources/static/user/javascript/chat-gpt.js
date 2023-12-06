const bodyChatGpt = document.querySelector("#chatGPT .card-body");
const chatGptInput = document.getElementById('chat-gpt-input');

// in chat window: scroll to end conversation when window is shown
$('#chatGPT').on('shown.bs.collapse', () => {
    cardBodyScrollEnd()
})

let isGptTyping = false;
chatGptInput.addEventListener('keyup', (e) => {
    if (isGptTyping || e.keyCode != 13) return;
    const value = e.target.value;
    e.target.value = null;
    appendMessageComponent({isUser: true, content: value});
    fetch(`/chat-gpt/stream-message?userInput=${value}`, {
        method: 'post',
    })
        .then(response => {
            const reader = response.body.getReader();
            const message = appendMessageComponent({ isUser: false, content: "" });
            const content = message.querySelector("p");
            isGptTyping = true;
            function read() {
                reader.read()
                    .then(({done, value}) => {
                        if (done) {
                            isGptTyping = false;
                            return;
                        }
                        const text = new TextDecoder().decode(value);
                        console.log(text.replace(/data:/g, '').replace(/\n/g, ''))
                        content.innerText += text.replace(/data:/g, '').replace(/\n/g, '');
                        cardBodyScrollEnd();
                        read()
                    })
                    .catch(() => isGptTyping = false)
            }
            read();
        })
        .catch(() => isGptTyping = false)
})

const cardBodyScrollEnd = () => {
    bodyChatGpt.scrollTo({
        behavior: 'smooth',
        top: bodyChatGpt.scrollHeight
    })
}

const messageComponent = ({ isUser, content }) => {
    const wrapper = document.createElement("div");
    wrapper.classList.add("d-flex", "mb-4");
    wrapper.style.flexDirection = isUser ? "row-reverse" : "row";

    const div = document.createElement("div");
    div.classList.add("p-3", "rounded");
    if (isUser) {
        div.classList.add("border", "mr-2");
        div.style.backgroundColor = "#fbfbfb";
    } else {
        div.classList.add("ml-2");
        div.style.backgroundColor = "rgba(57, 192, 237, 0.2";
    }

    const p = document.createElement("p");
    p.classList.add("small", "mb-0");
    p.textContent = content;
    div.appendChild(p);

    const img = document.createElement("img");
    if (isUser) {
        img.src = "/resources/img/icon/ava2-bg.webp";
    } else {
        img.src = "/resources/img/icon/chatgpt-icon.png";
    }
    img.style.width = "45px";
    img.style.height = "45px";

    wrapper.appendChild(img);
    wrapper.appendChild(div);
    return wrapper;
};

const appendMessageComponent = (props) => {
    const message = messageComponent(props);
    bodyChatGpt.appendChild(message);
    return message;
};