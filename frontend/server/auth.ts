import express from 'express';

import { getNewAuthToken } from './utils';

const router = express.Router();

router.post('/login', (req, res) => {
  const { body } = req;
  const isAuthRequest = body?.accessToken && body?.refreshToken && body?.expiredIn;

  if (!isAuthRequest) {
    res.status(400).send('올바른 요청 양식이 아닙니다.');

    return;
  }

  res
    .cookie('refreshToken', body.refreshToken, {
      httpOnly: true,
      maxAge: body.expiredIn,
    })
    .status(200)
    .send('true');
});

router.post('/logout', (_, res) => {
  res.clearCookie('refreshToken').status(200).send('true');
});

router.post('/renewToken', async (req, res) => {
  const { accessToken, refreshToken, expiredIn } = await getNewAuthToken(req);

  res
    .cookie('refreshToken', refreshToken, {
      httpOnly: true,
      maxAge: expiredIn,
    })
    .status(200)
    .json({ accessToken });
});

export default router;
