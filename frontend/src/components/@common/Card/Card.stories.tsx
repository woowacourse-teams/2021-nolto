import React from 'react';

import Card from './Card';

export default {
  title: 'components/common/Card',
  component: Card,
  argTypes: {},
};

export const Regular = () => <Card.Regular>Hot Toy용 카드입니다~</Card.Regular>;

export const Stretch = () => <Card.Stretch>Recent Toy용 카드입니다~</Card.Stretch>;
