import React from 'react';
import ReactDOMServer from 'react-dom/server';
import { StaticRouter } from 'react-router';
import { ServerStyleSheet, StyleSheetManager } from 'styled-components';
import { ChunkExtractor } from '@loadable/server';

import express from 'express';
import path from 'path';
import fs from 'fs';

import App from '../src/App';

const PORT = process.env.PORT || 9000;
const app = express();
const sheet = new ServerStyleSheet();

app.use(express.json());

app.use(express.static(path.resolve(__dirname, '../dist'), { index: false }));

const statsFile = path.resolve(__dirname, '../dist/loadable-stats.json');

const extractor = new ChunkExtractor({ statsFile });

app.get(['/', '/about', '/feeds/:feedId'], async (req, res) => {
  const jsx = extractor.collectChunks(
    <StyleSheetManager sheet={sheet.instance}>
      <StaticRouter location={req.url}>
        <App />
      </StaticRouter>
    </StyleSheetManager>,
  );

  const scriptTags = extractor.getScriptTags();

  const reactApp = ReactDOMServer.renderToString(jsx);

  const styleTags = sheet.getStyleTags();

  const indexFile = path.resolve(__dirname, '../dist/index.html');

  fs.readFile(indexFile, 'utf8', (err, data) => {
    if (err) {
      console.error('Node.js 서버에서 에러가 발생했습니다.', err);
      return res.status(500).send('서버에서 에러가 발생했습니다. 🔫');
    }

    const result = data
      .replace('<div id="root"></div>', `<div id="root">${reactApp}</div>`)
      .replace(/<head>(.+)<\/head>/s, `<head>$1 ${styleTags} ${scriptTags}</head>`);

    return res.send(result);
  });
});

app.listen(PORT, () => {
  console.log(`Server is listening on port ${PORT}`);
});
