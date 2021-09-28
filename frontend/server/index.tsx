import React from 'react';
import express from 'express';
import ReactDOMServer from 'react-dom/server';

import path from 'path';
import fs from 'fs';

import App from '../src/App';

const PORT = process.env.PORT || 4000;
const app = express();

app.get('/', (req, res) => {
  const app = ReactDOMServer.renderToString(<App />);

  const indexFile = path.resolve(__dirname, '../dist/index.html');

  fs.readFile(indexFile, 'utf8', (err, data) => {
    if (err) {
      console.error('Node.js 서버에서 에러가 발생했습니다.', err);
      return res.status(500).send('서버에서 에러가 발생했습니다. 🔫');
    }

    return res.send(data.replace('<div id="root"></div>', `<div id="root">${app}</div>`));
  });
});

app.use(express.static(path.resolve(__dirname, '../dist')));

app.listen(PORT, () => {
  console.log(`Server is listening on port ${PORT}`);
});
