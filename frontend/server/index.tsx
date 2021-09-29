import React from 'react';
import express from 'express';
import ReactDOMServer from 'react-dom/server';
import { StaticRouter } from 'react-router';

import path from 'path';
import fs from 'fs';

import App from '../src/App';

const PORT = process.env.PORT || 9000;
const app = express();

app.use(express.json());

app.get('/', (req, res) => {
  const reactApp = ReactDOMServer.renderToString(
    <StaticRouter location={req.url}>
      <App />
    </StaticRouter>,
  );

  const indexFile = path.resolve(__dirname, '../index.html');

  fs.readFile(indexFile, 'utf8', (err, data) => {
    if (err) {
      console.error('Node.js 서버에서 에러가 발생했습니다.', err);
      return res.status(500).send('서버에서 에러가 발생했습니다. 🔫');
    }

    return res.send(data.replace('<div id="root"></div>', `<div id="root">${reactApp}</div>`));
  });
});

app.use(express.static(path.resolve(__dirname, '..')));

app.listen(PORT, () => {
  console.log(`Server is listening on port ${PORT}`);
});
