import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter } from 'react-router-dom';
import * as Sentry from '@sentry/react';
import { Integrations } from '@sentry/tracing';

import hasWindow from 'constants/windowDetector';
import App from './App';

Sentry.init({
  dsn: process.env.SENTRY_DSN,
  integrations: [new Integrations.BrowserTracing()],
  tracesSampleRate: 1.0,
  enabled: true,
});

if (process.env.KAKAO_API_KEY && !window.Kakao.isInitialized()) {
  if (hasWindow) {
    window.Kakao.init(process.env.KAKAO_API_KEY);
  }
}

ReactDOM.hydrate(
  <React.StrictMode>
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </React.StrictMode>,
  document.getElementById('root'),
);
