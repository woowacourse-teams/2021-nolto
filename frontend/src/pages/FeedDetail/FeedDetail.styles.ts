import styled from 'styled-components';

import HighlightedText from 'components/@common/HighlightedText/HighlightedText';
import TextButton from 'components/@common/TextButton/TextButton';
import DotsIcon from 'assets/dots.svg';
import SOSFlag from 'assets/sosFlag.svg';
import StacksMoreIcon from 'assets/stacksMore.svg';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 8rem 0rem;
`;

const IntroContainer = styled.div`
  display: flex;
  gap: 4.25rem;
  margin-bottom: 2.5rem;
  width: 52rem;
`;

const IconsContainer = styled.div`
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-top: 0.25rem;
`;

const IconWrapper = styled.div`
  display: flex;
  align-items: center;
  gap: 0.375rem;
`;

const ThumbnailContainer = styled.div`
  position: relative;
  display: flex;
  align-items: flex-start;
`;

const Thumbnail = styled.img`
  width: 14rem;
  height: 14rem;
  border-radius: 0.5rem;
`;

const SOSFlagIcon = styled(SOSFlag)`
  position: absolute;
  top: -40%;
  right: -10%;
`;

const TitleContainer = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
`;

const TitleWrapper = styled.div`
  display: flex;
  align-items: center;
  flex: 1;
  gap: 0.5rem;
`;

const UserWrapper = styled.div`
  display: flex;
  align-items: center;
`;

const UserName = styled.span`
  font-size: 1rem;
`;

const UserImage = styled.img`
  width: 2rem;
  height: 2rem;
  border-radius: 50%;
  margin: 0.75rem;
`;

const DetailsContent = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
  margin-top: 1.5rem;
`;

const DetailsPair = styled.div`
  display: flex;
  align-items: center;
  gap: 1.75rem;
`;

const DetailsKey = styled(HighlightedText)`
  font-size: 1rem;
  font-weight: 400;
`;

const DetailsValue = styled.span`
  display: flex;
  font-size: 1rem;
  align-items: center;
  gap: 0.5rem;

  & > a {
    &:hover {
      text-decoration: underline;
    }
  }
`;

export const Tag = styled(TextButton.Rounded)`
  width: fit-content;
  height: 1.5rem;
  padding: 0 0.75rem;
`;

export const StacksMoreButton = styled(StacksMoreIcon)`
  cursor: pointer;
`;

const DotsDivider = styled(DotsIcon)`
  display: block;
  margin: auto;
`;

const Description = styled.pre`
  margin: 5rem auto 0 auto;
  width: 52rem;
  font-size: 1rem;
  line-height: 1.5rem;
  text-align: justify;
`;

export default {
  Root,
  IntroContainer,
  IconsContainer,
  IconWrapper,
  ThumbnailContainer,
  Thumbnail,
  SOSFlagIcon,
  TitleContainer,
  TitleWrapper,
  UserWrapper,
  UserName,
  UserImage,
  DetailsContent,
  DetailsPair,
  DetailsKey,
  DetailsValue,
  DotsDivider,
  Description,
};
