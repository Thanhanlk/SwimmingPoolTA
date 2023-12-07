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
                        const array = new Uint8Array(value);
                        const text = new TextDecoder().decode(array);
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
    wrapper.classList.add("message", `message-${isUser ? 'USER' : 'ASSISTANT'}`);

    const img = document.createElement("div");
    img.classList.add("image");

    const div = document.createElement("div");
    div.classList.add('content')
    const p = document.createElement("p");
    p.classList.add("small", "mb-0");

    p.textContent = content;
    div.appendChild(p);

    wrapper.appendChild(img);
    wrapper.appendChild(div);
    return wrapper;
};

const appendMessageComponent = (props) => {
    const message = messageComponent(props);
    bodyChatGpt.appendChild(message);
    return message;
};
