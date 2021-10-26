import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter } from 'react-router-dom';
import { Hydrate, QueryClient, QueryClientProvider } from 'react-query';
import * as Sentry from '@sentry/react';
import { Integrations } from '@sentry/tracing';
import { loadableReady } from '@loadable/component';
import { HelmetProvider } from 'react-helmet-async';

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

const dehydratedState = hasWindow && window.__REACT_QUERY_STATE__;

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      suspense: true,
      useErrorBoundary: true,
      retry: 1,
    },
  },
});

loadableReady(() => {
  ReactDOM.hydrate(
    <BrowserRouter>
      <QueryClientProvider client={queryClient}>
        <HelmetProvider>
          <Hydrate state={dehydratedState}>
            <App />
          </Hydrate>
        </HelmetProvider>
      </QueryClientProvider>
    </BrowserRouter>,
    document.getElementById('root'),
  );
});
