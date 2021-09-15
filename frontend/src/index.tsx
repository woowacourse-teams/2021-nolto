import React from 'react';
import ReactDOM from 'react-dom';
import * as Sentry from '@sentry/react';
import { Integrations } from '@sentry/tracing';

import App from './App';

Sentry.init({
  dsn: process.env.SENTRY_DSN,
  integrations: [new Integrations.BrowserTracing()],

  tracesSampleRate: 1.0,
  enabled: true,
});

if (process.env.KAKAO_API_KEY && !window.Kakao.isInitialized()) {
  window.Kakao.init(process.env.KAKAO_API_KEY);
}

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById('root'),
);
