/* app.js - Nueva SPA para gestión de Clientes y Correos
   - Alta de clientes con nombre, apellido, DNI y correo (2 pasos contra backend actual)
   - Búsqueda por parte del DNI o del correo (filtrado en cliente)
   - CRUD completo para clientes y correos
*/

(() => {
  'use strict';

  // ---------------------- DOM refs ----------------------
  const viewEl = document.getElementById('view');
  const titleEl = document.getElementById('view-title');
  const btnAdd = document.getElementById('btn-add');
  const modal = document.getElementById('modal');
  const modalBody = document.getElementById('modal-body');
  const modalClose = document.getElementById('modal-close');

  // ---------------------- State ------------------------
  let currentRoute = 'clientesCorreos';
  let onAddAction = null; // se redefine por vista

  // ---------------------- Utils ------------------------
  const byId = (id) => document.getElementById(id);
  const el = (tag, cls) => { const e = document.createElement(tag); if (cls) e.className = cls; return e; };
  const escapeHtml = (s) => String(s ?? '').replace(/[&<>\"']/g, m => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[m]));
  const asArray = (x) => Array.isArray(x) ? x : (x != null ? [x] : []);

  function openModal(node) { modalBody.innerHTML = ''; modalBody.appendChild(node); modal.setAttribute('aria-hidden','false'); }
  function closeModal() { modal.setAttribute('aria-hidden','true'); modalBody.innerHTML = ''; }
  modalClose?.addEventListener('click', closeModal);

  function toast(msg) { const p = el('p'); p.textContent = msg; p.style.marginTop = '8px'; p.style.color = '#c33'; return p; }

  // Normalizadores de respuestas variables
  function normJoinRow(row){
    // Puede venir como objeto con claves, o como arreglo (Object[]) por nativeQuery
    if (row && !Array.isArray(row) && typeof row === 'object') {
      return {
        dni: row.dni ?? row.DNI ?? row.cliente06dnifk ?? row.cliente06DNIfk ?? '',
        nombre: row.nombre ?? row.Nombre ?? '',
        apellido: row.apellido ?? row.Apellido ?? '',
        idCorreo: row.idCorreo ?? row.id_correo ?? row.id ?? '',
        correo: row.correo ?? row.Correo ?? ''
      };
    }
    if (Array.isArray(row)) {
      return {
        dni: row[0] ?? '',
        apellido: row[1] ?? '',
        nombre: row[2] ?? '',
        idCorreo: row[3] ?? '',
        correo: row[4] ?? ''
      };
    }
    return { dni:'', nombre:'', apellido:'', idCorreo:'', correo:'' };
  }
  function normCorreo(p){
    return {
      idCorreo: p?.idCorreo ?? p?.id_correo ?? p?.id ?? '',
      correo: p?.correo ?? '',
      dni: String(p?.cliente06Dni ?? p?.cliente06dni ?? p?.dni ?? '')
    };
  }

  // ---------------------- API helpers ------------------
  const api = {
    getJson: (url) => fetch(url).then(async r => { if (!r.ok) throw new Error(`${r.status} ${r.statusText}`); return r.json(); }),
    getText: (url) => fetch(url).then(async r => { const t = await r.text(); if (!r.ok) throw new Error(t || `${r.status} ${r.statusText}`); return t; }),
    postText: (url) => fetch(url, { method:'POST' }).then(async r => { const t = await r.text(); if (!r.ok) throw new Error(t || `${r.status} ${r.statusText}`); return t; }),
    deleteText: (url) => fetch(url, { method:'DELETE' }).then(async r => { const t = await r.text(); if (!r.ok) throw new Error(t || `${r.status} ${r.statusText}`); return t; })
  };

  // ---------------------- Formularios ------------------
  function formCliente({dni='', nombre='', apellido='', correo=''} = {}, {includeCorreo=true, readOnlyDni=false} = {}){
    const card = el('div','form');
    card.innerHTML = `
      <h3>${dni? 'Editar cliente' : 'Nuevo cliente'}</h3>
      <label>DNI<input id="f-dni" type="text" maxlength="20" ${readOnlyDni? 'readonly' : ''} value="${escapeHtml(dni)}" /></label>
      <label>Nombre<input id="f-nombre" type="text" maxlength="50" value="${escapeHtml(nombre)}" /></label>
      <label>Apellido<input id="f-apellido" type="text" maxlength="50" value="${escapeHtml(apellido)}" /></label>
      ${includeCorreo ? `<label>Correo<input id=\"f-correo\" type=\"email\" maxlength=\"100\" value=\"${escapeHtml(correo)}\" /></label>` : ''}
      <div class="actions">
        <button id="f-ok" class="btn primary">Guardar</button>
        <button id="f-cancel" class="btn">Cancelar</button>
      </div>`;
    card.querySelector('#f-cancel').onclick = closeModal;
    return card;
  }
  function formCorreo({idCorreo='', dni='', correo=''} = {}, {readOnlyId=false} = {}){
    const card = el('div','form');
    card.innerHTML = `
      <h3>${idCorreo? 'Editar correo' : 'Nuevo correo'}</h3>
      ${idCorreo ? `<label>ID Correo<input id=\"f-id\" type=\"text\" ${readOnlyId? 'readonly' : ''} value=\"${escapeHtml(String(idCorreo))}\" /></label>` : ''}
      <label>DNI Cliente<input id="f-dni" type="text" maxlength="20" value="${escapeHtml(dni)}" /></label>
      <label>Correo<input id="f-correo" type="email" maxlength="100" value="${escapeHtml(correo)}" /></label>
      <div class="actions">
        <button id="f-ok" class="btn primary">Guardar</button>
        <button id="f-cancel" class="btn">Cancelar</button>
      </div>`;
    card.querySelector('#f-cancel').onclick = closeModal;
    return card;
  }

  // ---------------------- Render: Clientes -------------
  async function renderClientes(){
    titleEl.textContent = 'Clientes';
    onAddAction = () => {
      const card = formCliente({}, {includeCorreo:true});
      openModal(card);
      card.querySelector('#f-ok').onclick = async () => {
        const dni = byId('f-dni').value.trim();
        const nombre = byId('f-nombre').value.trim();
        const apellido = byId('f-apellido').value.trim();
        const correo = byId('f-correo').value.trim();
        if (!dni || !nombre || !apellido || !correo) { card.appendChild(toast('Completa todos los campos')); return; }
        try {
          // 1) Alta cliente (puede responder 201 o 409 existente)
          try { await api.getText(`/clientes/guardar/${encodeURIComponent(dni)}/${encodeURIComponent(nombre)}/${encodeURIComponent(apellido)}`); } catch(e){ /* si existe, seguimos */ }
          // 2) Alta correo
          await api.getText(`/correos/guardar/${encodeURIComponent(dni)}/${encodeURIComponent(correo)}`);
          closeModal();
          await renderClientes();
        } catch(err){ card.appendChild(toast(err.message)); }
      };
    };

    viewEl.innerHTML = `<div class="card">
      <div class="toolbar">
        <input id="q" type="search" placeholder="Buscar por DNI, nombre o apellido..." />
      </div>
      <table class="table" id="tbl-clientes">
        <thead><tr><th>DNI</th><th>Nombre</th><th>Apellido</th><th>Acciones</th></tr></thead>
        <tbody><tr><td colspan="4">Cargando...</td></tr></tbody>
      </table>
    </div>`;

    try {
      const data = await api.getJson('/clientes/listartodos');
      const tb = document.querySelector('#tbl-clientes tbody');
      const renderRows = (rows) => {
        tb.innerHTML = '';
        asArray(rows).forEach(c => {
          const tr = el('tr');
          tr.innerHTML = `
            <td>${escapeHtml(c.dni)}</td>
            <td>${escapeHtml(c.nombre)}</td>
            <td>${escapeHtml(c.apellido)}</td>
            <td>
              <button class="btn small" data-act="edit" data-dni="${escapeHtml(c.dni)}">Editar</button>
              <button class="btn small danger" data-act="del" data-dni="${escapeHtml(c.dni)}">Borrar</button>
            </td>`;
          tb.appendChild(tr);
        });
      };
      renderRows(data);

      // Filtro
      byId('q').addEventListener('input', (e) => {
        const q = e.target.value.toLowerCase();
        const filtered = asArray(data).filter(c =>
          (c.dni||'').toLowerCase().includes(q) ||
          (c.nombre||'').toLowerCase().includes(q) ||
          (c.apellido||'').toLowerCase().includes(q)
        );
        renderRows(filtered);
      });

      // Acciones
      tb.addEventListener('click', async (e) => {
        const btn = e.target.closest('button[data-act]');
        if (!btn) return;
        const dni = btn.dataset.dni;
        if (btn.dataset.act === 'edit') {
          const card = formCliente({dni}, {includeCorreo:false, readOnlyDni:true});
          openModal(card);
          card.querySelector('#f-ok').onclick = async () => {
            const nombre = byId('f-nombre').value.trim();
            const apellido = byId('f-apellido').value.trim();
            if (!nombre || !apellido) { card.appendChild(toast('Completa nombre y apellido')); return; }
            try { await api.postText(`/clientes/actualizar/${encodeURIComponent(dni)}/${encodeURIComponent(nombre)}/${encodeURIComponent(apellido)}`); closeModal(); await renderClientes(); } catch(err){ card.appendChild(toast(err.message)); }
          };
        } else if (btn.dataset.act === 'del') {
          if (!confirm(`¿Borrar cliente ${dni}?`)) return;
          try { await api.deleteText(`/clientes/borrar/${encodeURIComponent(dni)}`); await renderClientes(); } catch(err){ alert(err.message); }
        }
      });
    } catch(err){
      viewEl.innerHTML = `<div class="card"><p>Error: ${escapeHtml(err.message)}</p></div>`;
    }
  }

  // ---------------------- Render: Correos --------------
  async function renderCorreos(){
    titleEl.textContent = 'Correos';
    onAddAction = () => {
      const card = formCorreo();
      openModal(card);
      card.querySelector('#f-ok').onclick = async () => {
        const dni = byId('f-dni').value.trim();
        const correo = byId('f-correo').value.trim();
        if (!dni || !correo) { card.appendChild(toast('Completa DNI y correo')); return; }
        try { await api.getText(`/correos/guardar/${encodeURIComponent(dni)}/${encodeURIComponent(correo)}`); closeModal(); await renderCorreos(); } catch(err){ card.appendChild(toast(err.message)); }
      };
    };

    viewEl.innerHTML = `<div class="card">
      <div class="toolbar">
        <input id="q" type="search" placeholder="Buscar por DNI o correo..." />
      </div>
      <table class="table" id="tbl-correos">
        <thead><tr><th>ID</th><th>Correo</th><th>DNI</th><th>Acciones</th></tr></thead>
        <tbody><tr><td colspan="4">Cargando...</td></tr></tbody>
      </table>
    </div>`;

    try {
      const data = await api.getJson('/correos/listartodos');
      const rows = asArray(data).map(normCorreo);
      const tb = document.querySelector('#tbl-correos tbody');
      const renderRows = (list) => {
        tb.innerHTML = '';
        list.forEach(c => {
          const tr = el('tr');
          tr.innerHTML = `
            <td>${escapeHtml(String(c.idCorreo))}</td>
            <td>${escapeHtml(c.correo)}</td>
            <td>${escapeHtml(String(c.dni))}</td>
            <td>
              <button class="btn small" data-act="edit" data-id="${escapeHtml(String(c.idCorreo))}" data-correo="${escapeHtml(c.correo)}" data-dni="${escapeHtml(String(c.dni))}">Editar</button>
              <button class="btn small danger" data-act="del" data-id="${escapeHtml(String(c.idCorreo))}">Borrar</button>
            </td>`;
          tb.appendChild(tr);
        });
      };
      renderRows(rows);

      byId('q').addEventListener('input', (e) => {
        const q = e.target.value.toLowerCase();
        renderRows(rows.filter(c => String(c.dni).toLowerCase().includes(q) || (c.correo||'').toLowerCase().includes(q)));
      });

      tb.addEventListener('click', async (e) => {
        const btn = e.target.closest('button[data-act]');
        if (!btn) return;
        if (btn.dataset.act === 'edit') {
          const id = btn.dataset.id;
          const card = formCorreo({idCorreo:id, dni:btn.dataset.dni, correo:btn.dataset.correo}, {readOnlyId:true});
          openModal(card);
          card.querySelector('#f-ok').onclick = async () => {
            const nuevoCorreo = byId('f-correo').value.trim();
            if (!nuevoCorreo) { card.appendChild(toast('Correo requerido')); return; }
            try { await api.postText(`/correos/actualizar/${encodeURIComponent(id)}/${encodeURIComponent(nuevoCorreo)}`); closeModal(); await renderCorreos(); } catch(err){ card.appendChild(toast(err.message)); }
          };
        } else if (btn.dataset.act === 'del') {
          const id = btn.dataset.id;
          if (!confirm(`¿Borrar correo ${id}?`)) return;
          try { await api.deleteText(`/correos/borrar/${encodeURIComponent(id)}`); await renderCorreos(); } catch(err){ alert(err.message); }
        }
      });
    } catch(err){
      viewEl.innerHTML = `<div class="card"><p>Error: ${escapeHtml(err.message)}</p></div>`;
    }
  }

  // -------------- Render: Clientes-Correos (JOIN) -----
  async function renderClientesCorreos(){
    titleEl.textContent = 'Clientes - Correos';
    onAddAction = () => {
      const card = formCliente({}, {includeCorreo:true});
      openModal(card);
      card.querySelector('#f-ok').onclick = async () => {
        const dni = byId('f-dni').value.trim();
        const nombre = byId('f-nombre').value.trim();
        const apellido = byId('f-apellido').value.trim();
        const correo = byId('f-correo').value.trim();
        if (!dni || !nombre || !apellido || !correo) { card.appendChild(toast('Completa todos los campos')); return; }
        try {
          try { await api.getText(`/clientes/guardar/${encodeURIComponent(dni)}/${encodeURIComponent(nombre)}/${encodeURIComponent(apellido)}`); } catch(e) { /* ignorar conflicto */ }
          await api.getText(`/correos/guardar/${encodeURIComponent(dni)}/${encodeURIComponent(correo)}`);
          closeModal();
          await renderClientesCorreos();
        } catch(err){ card.appendChild(toast(err.message)); }
      };
    };

    viewEl.innerHTML = `<div class="card">
      <div class="toolbar">
        <input id="q" type="search" placeholder="Buscar por DNI, nombre, apellido o correo..." />
      </div>
      <table class="table" id="tbl-cli-cor">
        <thead><tr><th>DNI</th><th>Nombre</th><th>Apellido</th><th>Correo</th><th>Acciones</th></tr></thead>
        <tbody><tr><td colspan="5">Cargando...</td></tr></tbody>
      </table>
    </div>`;

    try {
      const raw = await api.getJson('/clientes/listarClientesCorreos');
      const rows = asArray(raw).map(normJoinRow);
      const tb = document.querySelector('#tbl-cli-cor tbody');
      const renderRows = (list) => {
        tb.innerHTML = '';
        list.forEach(r => {
          const tr = el('tr');
          tr.innerHTML = `
            <td>${escapeHtml(r.dni)}</td>
            <td>${escapeHtml(r.nombre)}</td>
            <td>${escapeHtml(r.apellido)}</td>
            <td>${escapeHtml(r.correo)}</td>
            <td>
              <button class="btn small" data-act="edit-cli" data-dni="${escapeHtml(r.dni)}">Editar cliente</button>
              ${r.idCorreo? `<button class=\"btn small\" data-act=\"edit-mail\" data-id=\"${escapeHtml(String(r.idCorreo))}\" data-correo=\"${escapeHtml(r.correo)}\" data-dni=\"${escapeHtml(r.dni)}\">Editar correo</button>` : ''}
              <button class="btn small danger" data-act="del-cli" data-dni="${escapeHtml(r.dni)}">Borrar cliente</button>
              ${r.idCorreo? `<button class=\"btn small danger\" data-act=\"del-mail\" data-id=\"${escapeHtml(String(r.idCorreo))}\">Borrar correo</button>` : ''}
            </td>`;
          tb.appendChild(tr);
        });
      };
      renderRows(rows);

      byId('q').addEventListener('input', (e) => {
        const q = e.target.value.toLowerCase();
        renderRows(rows.filter(r =>
          (r.dni||'').toLowerCase().includes(q) ||
          (r.nombre||'').toLowerCase().includes(q) ||
          (r.apellido||'').toLowerCase().includes(q) ||
          (r.correo||'').toLowerCase().includes(q)
        ));
      });

      tb.addEventListener('click', async (e) => {
        const btn = e.target.closest('button[data-act]');
        if (!btn) return;
        const act = btn.dataset.act;
        if (act === 'edit-cli') {
          const dni = btn.dataset.dni;
          const card = formCliente({dni}, {includeCorreo:false, readOnlyDni:true});
          openModal(card);
          card.querySelector('#f-ok').onclick = async () => {
            const nombre = byId('f-nombre').value.trim();
            const apellido = byId('f-apellido').value.trim();
            if (!nombre || !apellido) { card.appendChild(toast('Completa nombre y apellido')); return; }
            try { await api.postText(`/clientes/actualizar/${encodeURIComponent(dni)}/${encodeURIComponent(nombre)}/${encodeURIComponent(apellido)}`); closeModal(); await renderClientesCorreos(); } catch(err){ card.appendChild(toast(err.message)); }
          };
        } else if (act === 'del-cli') {
          const dni = btn.dataset.dni;
          if (!confirm(`¿Borrar cliente ${dni}?`)) return;
          try { await api.deleteText(`/clientes/borrar/${encodeURIComponent(dni)}`); await renderClientesCorreos(); } catch(err){ alert(err.message); }
        } else if (act === 'edit-mail') {
          const id = btn.dataset.id;
          const card = formCorreo({idCorreo:id, dni:btn.dataset.dni, correo:btn.dataset.correo}, {readOnlyId:true});
          openModal(card);
          card.querySelector('#f-ok').onclick = async () => {
            const nuevoCorreo = byId('f-correo').value.trim();
            if (!nuevoCorreo) { card.appendChild(toast('Correo requerido')); return; }
            try { await api.postText(`/correos/actualizar/${encodeURIComponent(id)}/${encodeURIComponent(nuevoCorreo)}`); closeModal(); await renderClientesCorreos(); } catch(err){ card.appendChild(toast(err.message)); }
          };
        } else if (act === 'del-mail') {
          const id = btn.dataset.id;
          if (!confirm(`¿Borrar correo ${id}?`)) return;
          try { await api.deleteText(`/correos/borrar/${encodeURIComponent(id)}`); await renderClientesCorreos(); } catch(err){ alert(err.message); }
        }
      });
    } catch(err){
      viewEl.innerHTML = `<div class="card"><p>Error: ${escapeHtml(err.message)}</p></div>`;
    }
  }

  // ---------------------- Router ----------------------
  function route(){
    currentRoute = (location.hash || '#clientesCorreos').replace('#','');
    document.querySelectorAll('.nav-btn').forEach(n=>n.classList.remove('active'));
    const btn = document.querySelector(`.nav-btn[data-route="${currentRoute}"]`);
    btn?.classList.add('active');
    if (currentRoute === 'clientes') return renderClientes();
    if (currentRoute === 'correos') return renderCorreos();
    return renderClientesCorreos();
  }

  // ---------------------- Init ------------------------
  function init(){
    document.querySelectorAll('.nav-btn').forEach(b => b.addEventListener('click', (e) => {
      const r = e.currentTarget.dataset.route; location.hash = r;
    }));
    btnAdd?.addEventListener('click', () => { if (typeof onAddAction === 'function') onAddAction(); });
    window.addEventListener('hashchange', route);
    if (!location.hash) location.hash = '#clientesCorreos';
    route();
  }

  init();
})();
