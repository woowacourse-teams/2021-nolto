import React from 'react';
import { useParams } from 'react-router-dom';

import Loading from 'components/@common/Loading/Loading';
import useOAuthLogin from 'hooks/useOAuthLogin';
import { OAuthType } from 'types';

const OAuth = () => {
  const params = useParams<{ oauth: OAuthType }>();

  useOAuthLogin(params.oauth);

  return <Loading />;
};

export default OAuth;
