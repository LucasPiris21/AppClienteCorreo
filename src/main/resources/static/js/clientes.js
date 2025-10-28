fetch('/clientes/listartodos')
.then(clientes => clientes.json())
.then(clientes => {
    const tablaClientes = document.getElementById("tabla-clientes");
    clientes.forEach(cliente => {
        tablaClientes.innerHTML += `
    <tr>
        <td>${cliente.dni}</td>
        <td>${cliente.nombre}</td>
        <td>${cliente.apellido}</td>
        <td>
            <button type="button" class="btn-editar" onclick="window.location.href='editarClientes.html?dni=${cliente.dni}'">
            <svg
                xmlns="http://www.w3.org/2000/svg"
                width="32"
                height="32"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="1"
                stroke-linecap="round"
                stroke-linejoin="round"
            >
                <path d="M7 7h-1a2 2 0 0 0 -2 2v9a2 2 0 0 0 2 2h9a2 2 0 0 0 2 -2v-1" />
                <path d="M20.385 6.585a2.1 2.1 0 0 0 -2.97 -2.97l-8.415 8.385v3h3l8.385 -8.415z" />
                <path d="M16 5l3 3" />
            </svg>
            </button>

        </td>
        <td>
            <button class="btn-eliminar" value="${cliente.dni}">
                    <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="32"
                    height="32"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="1"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    >
                    <path d="M4 7l16 0" />
                    <path d="M10 11l0 6" />
                    <path d="M14 11l0 6" />
                    <path d="M5 7l1 12a2 2 0 0 0 2 2h8a2 2 0 0 0 2 -2l1 -12" />
                    <path d="M9 7v-3a1 1 0 0 1 1 -1h4a1 1 0 0 1 1 1v3" />
                    </svg>
                </button>
        </td>
    </tr>
    `;
    });

    const deleteButtons = Array.from(document.getElementsByClassName("btn-eliminar"));

    deleteButtons.forEach(button => {
        button.addEventListener("click", (e) => {
            e.preventDefault();
            if (confirm('¿Está seguro de querer eliminar este cliente?')) {
                const dni = button.value
                fetch(`clientes/borrar/${dni}`, {
                    method: "DELETE"
                })
                .then(async results => {
                    if (!results.ok) {
                        const message = await results.text();
                        return alert(message)
                    }

                    const message = await results.text();
                    alert(message)
                    window.location.replace(window.location.href);
                })
            };
        })
    });
})

document.getElementById("search").addEventListener("submit", (e) => {
    e.preventDefault()
    const searchTerm = document.getElementById("searchTerm").value.trim();
    fetch(`clientes/search?searchTerm=${searchTerm}`)
    .then(clientes => clientes.json())
    .then(clientes => {
        const tablaClientes = document.getElementById("tabla-clientes");
        tablaClientes.replaceChildren();
        clientes.forEach(cliente => {
            tablaClientes.innerHTML += `
        <tr>
            <td>${cliente.dni}</td>
            <td>${cliente.nombre}</td>
            <td>${cliente.apellido}</td>
            <td>
                <button type="button" class="btn-editar" onclick="window.location.href='editarClientes.html?dni=${cliente.dni}'">
                <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="32"
                    height="32"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="1"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                >
                    <path d="M7 7h-1a2 2 0 0 0 -2 2v9a2 2 0 0 0 2 2h9a2 2 0 0 0 2 -2v-1" />
                    <path d="M20.385 6.585a2.1 2.1 0 0 0 -2.97 -2.97l-8.415 8.385v3h3l8.385 -8.415z" />
                    <path d="M16 5l3 3" />
                </svg>
                </button>
    
            </td>
            <td>
                <button class="btn-eliminar" value="${cliente.dni}">
                        <svg
                        xmlns="http://www.w3.org/2000/svg"
                        width="32"
                        height="32"
                        viewBox="0 0 24 24"
                        fill="none"
                        stroke="currentColor"
                        stroke-width="1"
                        stroke-linecap="round"
                        stroke-linejoin="round"
                        >
                        <path d="M4 7l16 0" />
                        <path d="M10 11l0 6" />
                        <path d="M14 11l0 6" />
                        <path d="M5 7l1 12a2 2 0 0 0 2 2h8a2 2 0 0 0 2 -2l1 -12" />
                        <path d="M9 7v-3a1 1 0 0 1 1 -1h4a1 1 0 0 1 1 1v3" />
                        </svg>
                    </button>
            </td>
        </tr>
        `;
        });

        const deleteButtons = Array.from(document.getElementsByClassName("btn-eliminar"));

        deleteButtons.forEach(button => {
            button.addEventListener("click", (e) => {
                e.preventDefault();
                if (confirm('¿Está seguro de querer eliminar este cliente?')) {
                    const dni = button.value
                    fetch(`clientes/borrar/${dni}`, {
                        method: "DELETE"
                    })
                    .then(async results => {
                        if (!results.ok) {
                            const message = await results.text();
                            return alert(message)
                        }

                        const message = await results.text();
                        alert(message)
                        window.location.replace(window.location.href);
                    })
                };
            })
        });
    });
});