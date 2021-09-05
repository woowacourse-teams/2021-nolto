import styled from 'styled-components';

import HighlightedText from 'components/@common/HighlightedText/HighlightedText';
import TextButton from 'components/@common/TextButton/TextButton';
import SOSFlagComponent from 'components/@common/SOSFlag/SOSFlag';
import { MEDIA_QUERY } from 'constants/mediaQuery';
import StacksMoreIcon from 'assets/stacksMore.svg';
import { defaultShadow } from 'commonStyles';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  width: 100%;

  & h3 {
    margin-bottom: 0.25rem;
  }
`;

const IntroContainer = styled.div`
  display: flex;
  width: 100%;
  gap: 3.75rem;

  @media ${MEDIA_QUERY.TABLET} {
    gap: 2.5rem;
  }

  @media ${MEDIA_QUERY.TABLET_SMALL} {
    flex-direction: column;
  }
`;

const IconsContainer = styled.div`
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-top: 0.75rem;
`;

const IconWrapper = styled.div`
  display: flex;
  align-items: center;
  gap: 0.375rem;
`;

const ThumbnailContainer = styled.div`
  position: relative;
  display: flex;
  flex-direction: column;
  flex: 1;
  flex-shrink: 1;

  @media ${MEDIA_QUERY.TABLET_SMALL} {
    display: none;
  }
`;

const MobileThumbnailContainer = styled(ThumbnailContainer)`
  display: none;

  @media ${MEDIA_QUERY.TABLET_SMALL} {
    display: flex;
    margin: 1rem;
  }
`;

const Thumbnail = styled.div`
  position: relative;
  border-radius: 0.5rem;
  ${defaultShadow}

  &::after {
    content: '';
    display: block;
    padding-bottom: 100%;
  }

  & > img {
    position: absolute;
    object-fit: contain;
    height: 100%;
    width: 100%;
    left: 0;
    top: 0;
  }
`;

export const SOSFlag = styled(SOSFlagComponent)`
  position: absolute;
  left: -0.5rem;
  top: 0.75rem;
`;

const FeedSummaryContainer = styled.div`
  flex: 3;
  flex-shrink: 1;
`;

const TitleContainer = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
`;

const TitleWrapper = styled.div`
  flex: 1;
  margin: 4px 0;

  & > h2 {
    display: inline;
    margin-right: 0.5rem;
  }

  * {
    vertical-align: middle;
  }
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
  gap: 1.75rem;

  @media ${MEDIA_QUERY.TABLET_SMALL} {
    gap: 1rem;
  }
`;

const DetailsKeyWrapper = styled.div`
  display: flex;
  flex-basis: 6.5rem;
  flex-shrink: 0;

  @media ${MEDIA_QUERY.TABLET_SMALL} {
    flex-basis: 5.5rem;
  }
`;

const DetailsKey = styled(HighlightedText)`
  font-weight: 400;
`;

const DetailsValue = styled.span`
  display: flex;
  font-size: 1rem;
  align-items: center;
  gap: 0.5rem;
  flex-wrap: wrap;
  overflow: hidden;

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

const Description = styled.pre`
  margin: 4rem auto;
  font-size: 1rem;
  line-height: 1.5rem;
  text-align: justify;
  white-space: pre-wrap;

  @media ${MEDIA_QUERY.TABLET_SMALL} {
    margin: 2rem auto;
  }
`;

const CommentContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  padding-top: 1rem;
`;

export default {
  Root,
  IntroContainer,
  IconsContainer,
  IconWrapper,
  ThumbnailContainer,
  MobileThumbnailContainer,
  Thumbnail,
  FeedSummaryContainer,
  TitleContainer,
  TitleWrapper,
  UserWrapper,
  UserName,
  UserImage,
  DetailsContent,
  DetailsPair,
  DetailsKeyWrapper,
  DetailsKey,
  DetailsValue,
  Description,
  CommentContainer,
};
