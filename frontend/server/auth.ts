import express from 'express';

import { getNewAuthToken } from './utils';
import { AuthData } from 'types';

const router = express.Router();

router.post('/login', (req, res) => {
  const { body } = req;
  const isAuthRequest = body?.accessToken && body?.refreshToken;

  if (!isAuthRequest) {
    res.status(400).send('올바른 요청 양식이 아닙니다.');

    return;
  }

  const refreshToken = (body as AuthData).refreshToken;

  res
    .cookie('refreshToken', refreshToken.value, {
      httpOnly: true,
      maxAge: refreshToken.expiredIn,
    })
    .status(200)
    .send('true');
});

router.post('/logout', (_, res) => {
  res.clearCookie('refreshToken').status(200).send('true');
});

router.post('/renewToken', async (req, res) => {
  const authData = await getNewAuthToken(req);

  res
    .cookie('refreshToken', authData.refreshToken.value, {
      httpOnly: true,
      secure: true,
      maxAge: authData.refreshToken.expiredIn,
    })
    .status(200)
    .json(authData);
});

export default router;
